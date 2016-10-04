package ru.tinkoff.formatting.slots;

import java.util.Arrays;

/**
 * @author Mikhail Artemyev
 */
public final class SlotValidators {

    /**
     * SlotValidator that allows <b>any</b> characters to be input in the slot.
     */
    public static class GenerousValidator implements Slot.SlotValidator {

        @Override
        public boolean validate(char value) {
            return true;
        }

        /*
         * equals(Object) and hashCode() here are override in order to allow have only single
         * instance of this validator in a SlotValidatorSet (which is actually just a HashSet).
         */

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof GenerousValidator;
        }

        @Override
        public int hashCode() {
            return -56328;
        }
    }

    public static class DigitValidator implements Slot.SlotValidator {

        @Override
        public boolean validate(final char value) {
            return Character.isDigit(value);
        }

        /*
         * equals(Object) and hashCode() here are override in order to allow have only single
         * instance of this validator in a SlotValidatorSet (which is actually just a HashSet).
         */

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof DigitValidator;
        }

        @Override
        public int hashCode() {
            return -56329;
        }
    }

    public static class MaskedDigitValidator extends DigitValidator {

        private static final char[] DEFAULT_DIGIT_MASK_CHARS = {'X', 'x', '*'};
        private char[] maskChars = DEFAULT_DIGIT_MASK_CHARS;

        public MaskedDigitValidator() {
        }

        public MaskedDigitValidator(char... maskChars) {
            if (maskChars == null) {
                throw new IllegalArgumentException("Mask chars cannot be null");
            }
            this.maskChars = maskChars;
        }

        @Override
        public boolean validate(char value) {
            if (super.validate(value)) {
                return true;
            }

            for (char aChar : maskChars) {
                if (aChar == value) {
                    return true;
                }
            }

            return false;
        }

        /*
         * equals(Object) and hashCode() here are override in order to allow have only single
         * instance of this validator in a SlotValidatorSet (which is actually just a HashSet).
         */

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MaskedDigitValidator that = (MaskedDigitValidator) o;

            return Arrays.equals(maskChars, that.maskChars);

        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(maskChars);
        }
    }

    public static class LetterValidator implements Slot.SlotValidator {

        private final boolean supportsEnglish;
        private final boolean supportsRussian;

        public LetterValidator() {
            this(true, true);
        }

        public LetterValidator(final boolean supportsEnglish,
                               final boolean supportsRussian) {
            this.supportsEnglish = supportsEnglish;
            this.supportsRussian = supportsRussian;
        }

        @Override
        public boolean validate(final char value) {
            return validateEnglishLetter(value) || validateRussianLetter(value);
        }

        private boolean validateEnglishLetter(final char value) {
            return supportsEnglish == isEnglishCharacter(value); // true when both 0 or 1
        }

        private boolean validateRussianLetter(final char value) {
            final int code = (int) value;
            boolean russian = 'А' <= code && code <= 'я'; // 'А' is russian!!

            return supportsRussian == russian; // true when both 0 or 1
        }

        private boolean isEnglishCharacter(final int charCode) {
            return ('A' <= charCode && charCode <= 'Z') || ('a' <= charCode && charCode <= 'z');
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LetterValidator that = (LetterValidator) o;

            if (supportsEnglish != that.supportsEnglish) return false;
            return supportsRussian == that.supportsRussian;

        }

        @Override
        public int hashCode() {
            int result = (supportsEnglish ? 1 : 0);
            result = 31 * result + (supportsRussian ? 1 : 0);
            return result;
        }
    }


}