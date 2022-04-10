public enum EnumTest {
    AM {
        @Override
        public String handle() {
            return "am";
        }
    },
    PM {
        @Override
        public String handle() {
            return "pm";
        }
    };

    public abstract String handle();
}
