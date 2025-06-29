package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);
    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome,
            UserType.Type type) {

    }
    private final static Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
    private final static Queue<StaticUser> USERS_WITH_FRIEND = new ConcurrentLinkedDeque<>();
    private final static Queue<StaticUser> USERS_WITH_INCOME_REQUEST = new ConcurrentLinkedDeque<>();
    private final static Queue<StaticUser> USERS_WITH_OUTCOME_REQUEST = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USERS.add(new StaticUser("dove", "12345", null, null, null, UserType.Type.EMPTY));
        USERS_WITH_FRIEND.add(new StaticUser("goose", "67890", "swan", null, null, UserType.Type.WITH_FRIEND));
        USERS_WITH_INCOME_REQUEST.add(new StaticUser("penguin", "54321", null, "duck", null, UserType.Type.WITH_INCOME_REQUEST));
        USERS_WITH_OUTCOME_REQUEST.add(new StaticUser("duck", "12345", null, null, "penguin", UserType.Type.WITH_OUTCOME_REQUEST));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @SuppressWarnings({"unchecked", "ThrowableNotThrown"})
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(parameter -> Optional.of(parameter)
                        .map(p -> p.getAnnotation(UserType.class))
                        .ifPresent(
                                ut -> {
                                    Optional<StaticUser> user = Optional.empty();
                                    StopWatch sw = StopWatch.createStarted();
                                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                        user = Optional.ofNullable(switch (ut.value()) {
                                            case EMPTY -> EMPTY_USERS.poll();
                                            case WITH_FRIEND -> USERS_WITH_FRIEND.poll();
                                            case WITH_INCOME_REQUEST -> USERS_WITH_INCOME_REQUEST.poll();
                                            case WITH_OUTCOME_REQUEST -> USERS_WITH_OUTCOME_REQUEST.poll();
                                        });
                                    }
                                    Allure.getLifecycle().updateTestCase(testCase ->
                                            testCase.setStart(new Date().getTime()));
                                    user.ifPresentOrElse(
                                            u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                    .getOrComputeIfAbsent(
                                                            context.getUniqueId(),
                                                            value -> new HashMap<>()
                                                    )).put(ut, u),
                                            () -> new IllegalStateException("Can't find user after 30 second"));
                                }
                        ))
                ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class)).values()
                .forEach(user -> {
                    switch (user.type) {
                        case EMPTY -> EMPTY_USERS.add(user);
                        case WITH_FRIEND -> USERS_WITH_FRIEND.add(user);
                        case WITH_INCOME_REQUEST -> USERS_WITH_INCOME_REQUEST.add(user);
                        case WITH_OUTCOME_REQUEST -> USERS_WITH_OUTCOME_REQUEST.add(user);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        UserType annotation = parameterContext.getParameter().getAnnotation(UserType.class);
        return (StaticUser) extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(annotation);
    }
}
