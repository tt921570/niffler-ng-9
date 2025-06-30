package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.GhApiClient;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.test.SpendingTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexander
 */
public class IssueExtension implements ExecutionCondition {
    private static final GhApiClient ghApiClient = new GhApiClient();

    @SneakyThrows
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<DisabledByIssue> annotation;

        annotation = AnnotationSupport.findAnnotation(
                context.getRequiredTestClass(),
                DisabledByIssue.class,
                List.of(SpendingTest.class)
        );

        return annotation.map(
                byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue #" + byIssue.value())
                        : ConditionEvaluationResult.enabled("Issue closed")
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @DisabledByIssue not found")
        );
    }
}
