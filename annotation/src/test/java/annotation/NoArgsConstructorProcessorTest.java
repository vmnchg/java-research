package annotation;

/**
 * Created by vchhieng on 18/06/2015.
 */
public class NoArgsConstructorProcessorTest {

    @MustHavePublicMoArgsConstructor
    public abstract class NoArgsSuperClass {
        public NoArgsSuperClass() {
        }
    }

    // Passes
    public class PublicNoArgsConstructor extends NoArgsSuperClass {
        public PublicNoArgsConstructor() {
        }
    }

    // Passes
    public class DefaultConstructor extends NoArgsSuperClass {
    }

    // Passes
    public class SeveralConstructors extends NoArgsSuperClass {
        public SeveralConstructors(String as) {
        }

        public SeveralConstructors(int ai) {
        }

        public SeveralConstructors() {
        }
    }

/*    // Fails
    public class NonPublicConstructor extends NoArgsSuperClass {
        NonPublicConstructor() {
        }
    }

    // Fails
    public class WrongConstructor extends NoArgsSuperClass {
        public WrongConstructor(String aString) {
        }
    }*/
}
