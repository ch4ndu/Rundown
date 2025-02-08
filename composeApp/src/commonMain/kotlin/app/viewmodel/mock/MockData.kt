package app.viewmodel.mock

import data.database.model.Budget
import data.database.model.transaction.FireFlyTransaction
import data.enums.BudgetType
import domain.currentDate
import domain.minusDaysAtBeginning
import domain.minusMonths
import domain.model.BudgetSpending
import domain.model.CategorySpending
import domain.model.ExpenseData
import domain.model.ExpenseIncomeData

object MockData {

    val mockTransactions =
        listOf(
            FireFlyTransaction(
                description = "sample description",
                transactionType = "expense",
                destination_type = "destination",
                destination_name = "Checking Account",
                category_name = "coffee",
                amount = 10.12,
                transaction_journal_id = 1L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "sample description",
                transactionType = "transfer",
                destination_type = "destination",
                destination_name = "Credit1",
                category_name = "category",
                amount = 50.0,
                transaction_journal_id = 2L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "sample description",
                transactionType = "expense",
                destination_type = "Loooooong destination",
                destination_name = "Savings",
                category_name = "category",
                amount = 10.12,
                transaction_journal_id = 3L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "Comcast",
                transactionType = "expense",
                destination_type = "asset",
                destination_name = "Checking",
                category_name = "HomeExpense",
                amount = 10.12,
                transaction_journal_id = 4L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "Rent Payment",
                transactionType = "expense",
                destination_type = "asset",
                destination_name = "Checking",
                category_name = "HomeExpense",
                amount = 600.0,
                transaction_journal_id = 5L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "Chevron",
                transactionType = "expense",
                destination_type = "asset",
                destination_name = "Credit",
                category_name = "Gas",
                amount = 45.54,
                transaction_journal_id = 6L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            ),
            FireFlyTransaction(
                description = "Whole Foods",
                transactionType = "expense",
                destination_type = "asset",
                destination_name = "CreditCard",
                category_name = "Groceries",
                amount = 87.54,
                transaction_journal_id = 7L,
                destination_id = 1L,
                currency_name = "",
                currency_id = 1L,
                currency_decimal_places = 1,
                currency_symbol = "$",
                date = currentDate(),
                internal_reference = "sdf",
                budget_id = null,
                isPending = false,
                source_id = null,
                tags = emptyList(),
                user = 1,
                currency_code = "",
                budget_name = null,
                category_id = null,
                source_iban = null,
                source_name = null,
                source_type = null,
                notes = null,
                order = 1
            )
        )

    val mockExpenseList2 = listOf(
        ExpenseData(200f, currentDate()),
        ExpenseData(205f, currentDate().minusDaysAtBeginning(1)),
        ExpenseData(210f, currentDate().minusDaysAtBeginning(2)),
        ExpenseData(180f, currentDate().minusDaysAtBeginning(3)),
        ExpenseData(195f, currentDate().minusDaysAtBeginning(4)),
        ExpenseData(500f, currentDate().minusDaysAtBeginning(5)),
        ExpenseData(450f, currentDate().minusDaysAtBeginning(6)),
        ExpenseData(600f, currentDate().minusDaysAtBeginning(7)),
    )

    val mockExpenseList =
        listOf(
            ExpenseData(date = currentDate(), expenseAmount = 200f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(1), expenseAmount = 200f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(2), expenseAmount = 210f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(3), expenseAmount = 230f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(4), expenseAmount = 160f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(5), expenseAmount = 190f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(6), expenseAmount = 50f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(7), expenseAmount = 500f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(8), expenseAmount = 545f),
            ExpenseData(date = currentDate().minusDaysAtBeginning(9), expenseAmount = 460f),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(10),
                expenseAmount = 350f
            ),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(11),
                expenseAmount = 675f
            ),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(12),
                expenseAmount = 487f
            ),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(13),
                expenseAmount = 300f
            ),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(14),
                expenseAmount = 150f
            ),
            ExpenseData(
                date = currentDate().minusDaysAtBeginning(15),
                expenseAmount = 275f
            ),
        )

    val mockExpenseIncomeList = listOf(

        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate()
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(1)
        ),
        ExpenseIncomeData(
            expenseAmount = 20f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(2)
        ),
        ExpenseIncomeData(
            expenseAmount = 10f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(3)
        ),
        ExpenseIncomeData(
            expenseAmount = 5f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(4)
        ),
        ExpenseIncomeData(
            expenseAmount = 10f,
            incomeAmount = 500f,
            date = currentDate().minusMonths(5)
        ),
        ExpenseIncomeData(
            expenseAmount = 300f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(6)
        ),
        ExpenseIncomeData(
            expenseAmount = 20f,
            incomeAmount = 500f,
            date = currentDate().minusMonths(7)
        ),
        ExpenseIncomeData(
            expenseAmount = 50f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(8)
        ),
        ExpenseIncomeData(
            expenseAmount = 70f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(9)
        ),
        ExpenseIncomeData(
            expenseAmount = 30f,
            incomeAmount = 500f,
            date = currentDate().minusMonths(10)
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(11)
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(12)
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(13)
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(14)
        ),
        ExpenseIncomeData(
            expenseAmount = 200f,
            incomeAmount = 0f,
            date = currentDate().minusMonths(15)
        ),
    )


    val budgetSpendingList = listOf(
        BudgetSpending(
            budget = Budget(
                id = 1L,
                name = "Shopping",
                type = BudgetType.RESET,
                amount = 500L,
                period = null
            ),
            expenseIncomeDataList = mockExpenseList
        ),
        BudgetSpending(
            budget = Budget(
                id = 2L,
                name = "HomeExpenses",
                type = BudgetType.RESET,
                amount = 500L,
                period = null
            ),
            expenseIncomeDataList = mockExpenseList2
        ),

        )

    val categorySpendingList = listOf(
        CategorySpending(
            categoryName = "Shopping",
            totalExpenseSum = 300f,
            totalIncomeSum = 500f,
            expenseIncomeData = mockExpenseIncomeList
        ),
        CategorySpending(
            categoryName = "Groceries",
            totalExpenseSum = 300f,
            totalIncomeSum = 500f,
            expenseIncomeData = mockExpenseIncomeList
        ),
        CategorySpending(
            categoryName = "Car",
            totalExpenseSum = 300f,
            totalIncomeSum = 500f,
            expenseIncomeData = mockExpenseIncomeList
        ),
    )
}