package data.enums

enum class BudgetType {
    NONE, ROLLOVER, RESET;

    companion object {
        fun fromApi(type: String?): BudgetType {
            return if (ROLLOVER.name.equals(type, true)) {
                ROLLOVER
            } else if (RESET.name.equals(type, true)) {
                RESET
            } else {
                NONE
            }
        }
    }
}
