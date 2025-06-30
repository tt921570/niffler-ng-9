package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

/**
 * @author Alexander
 */
public class RandomDataUtils {

    private static final Faker FAKER = new Faker();

    public static String randomUsername() {
        return FAKER.name().username();
    }

    public static String randomName() {
        return FAKER.name().firstName();
    }

    public static String randomSurname() {
        return FAKER.name().lastName();
    }

    public static String randomCategoryName() {
        return FAKER.lorem().word();
    }

    public static String randomSentence(int wordsCount) {
        return FAKER.lorem().sentence(wordsCount);
    }
}
