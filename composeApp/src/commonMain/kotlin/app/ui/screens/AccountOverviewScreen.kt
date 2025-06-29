/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, KoinExperimentalAPI::class, ExperimentalLayoutApi::class
)

package app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.model.TotalsSummary
import app.theme.DarkGrey
import app.theme.FireflyAppTheme
import app.theme.HaloBlue
import app.ui.AccountOverview
import app.ui.AppBarWithBackAndOptions
import app.ui.PieChart
import app.ui.Savers
import app.ui.TopAppBarActionButton
import app.ui.TransactionItem
import app.ui.getTextColorForAmount
import app.ui.isScrollingUp
import app.ui.screenWidth
import app.viewmodel.AccountChartsViewModel
import app.viewmodel.AccountOverviewViewModel
import collectAsStateWithLifecycle
import data.database.model.transaction.FireFlyTransaction
import data.enums.ExpenseType
import domain.model.DateRange
import rundown.composeapp.generated.resources.Res
import rundown.composeapp.generated.resources.calendar_month_outline
import getDisplayWithCurrency
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lighthousegames.logging.logging

private const val TAG = "AccountOverviewScreen"
private val log = logging()

enum class OverviewTabs {
    Overview, Transactions, Tags, Categories
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOverviewScreen(
    accountOverviewViewModel: AccountOverviewViewModel = koinViewModel(),
    accountChartsViewModel: AccountChartsViewModel = koinViewModel(),
    accountType: String,
    accountId: String,
    accountName: String,
    onBack: () -> Unit,
    onAccountCashFlowDetailsClick: (DateRange) -> Unit
) {
    // TODO workaround until koin supports savedStateHandle
    LaunchedEffect(Unit) {
        accountOverviewViewModel.setAccountId(accountId)
        accountChartsViewModel.setAccountId(accountId)
        accountChartsViewModel.setAccountName(accountName)
    }
    val setActiveTag = { tag: String ->
        accountOverviewViewModel.activeTag.value = tag
    }

    val selectedDateRange = rememberSaveable(saver = Savers.DateRange) {
        mutableStateOf(
            DateRange.getDefaultRange()
        )
    }
    val accountCashFlowDetailsClickInternal = remember {
        {
            onAccountCashFlowDetailsClick.invoke(selectedDateRange.value)
        }
    }

    LaunchedEffect(Unit, selectedDateRange.value) {
        accountChartsViewModel.setDateRange(selectedDateRange.value)
        accountOverviewViewModel.setDateRange(selectedDateRange.value)
        setActiveTag.invoke("")
    }

    //top expenses
    val topExpenseList
            by accountOverviewViewModel.topTransactionsFlow
                .collectAsStateWithLifecycle(initialValue = emptyList())

    //LineChart data
    val chartData
            by accountChartsViewModel.chartDataFlow
                .collectAsStateWithLifecycle(initialValue = null)
    val chartDataList
            by accountChartsViewModel.lineDataSetFlowVico
                .collectAsStateWithLifecycle(initialValue = null)

    val spendingByMonth by accountChartsViewModel.spendingDataFlow.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )
    val cashFlowData by accountChartsViewModel.cashFlowData.collectAsStateWithLifecycle(initialValue = emptyList())

    //tags
    val activeTag =
        accountOverviewViewModel.activeTag.collectAsStateWithLifecycle(initialValue = "")
    val tagsForAccount = accountOverviewViewModel.tagsForAccountWithDate.collectAsState(
        initial = emptyList()
    )
    val tagSumForAccount = accountOverviewViewModel.tagSummaryAccount.collectAsStateWithLifecycle(
        initialValue = emptyMap()
    )
    val transactionForActiveTag =
        accountOverviewViewModel.transactionsForActiveTag.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
    // Categories
    val activeCategory =
        accountOverviewViewModel.activeCategory.collectAsStateWithLifecycle(initialValue = "")
    val categorySum =
        accountOverviewViewModel.categorySummaryAccount.collectAsStateWithLifecycle(
        initialValue = emptyMap()
    )
    val transactionsForActiveCategory =
        accountOverviewViewModel.transactionsForActiveCategory.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )
    val setActiveCategory = { category: String ->
        accountOverviewViewModel.activeCategory.value = category

    }

    val lazyPagingItems = remember(selectedDateRange.value) {
        accountOverviewViewModel.getNewTransactionListForAccount(
            accountType = accountType,
            accountId = accountId.toLong(),
            startDate = selectedDateRange.value.startDate,
            endDate = selectedDateRange.value.endDate
        )
    }
    val transactionItems = lazyPagingItems.collectAsStateWithLifecycle(initialValue = emptyList())
    val dimensions = FireflyAppTheme.dimensions
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    var sheetManuallyHidden by remember { mutableStateOf(false) }
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = false,
            confirmValueChange = { sheetValue ->
                log.d { "confirmValueChange:sheetValue:$sheetValue" }
                if (sheetValue == SheetValue.Hidden || sheetValue == SheetValue.PartiallyExpanded) {
                    sheetManuallyHidden = true
                }
                true
            }
        )
    )
    BottomSheetScaffold(
        scaffoldState = modalSheetState,
        sheetContent = {
            FilterScreen(
                dateRange = selectedDateRange.value,
                onClose = {
                    sheetManuallyHidden = false
                    scope.launch { modalSheetState.bottomSheetState.partialExpand() }
                },
                onSave = {
                    selectedDateRange.value = it
                    scope.launch {
                        sheetManuallyHidden = false
                        modalSheetState.bottomSheetState.partialExpand()
                    }
                }
            )
        },
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(BottomSheetDefaults.SheetPeekHeight)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = selectedDateRange.value.getFilterDisplay()
                )
            }
        }) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ShowTopBarWithCalendar(
                    accountName = accountName,
                    modalSheetState = modalSheetState,
                    onBack = onBack,
                    refreshClicked = {
                        accountOverviewViewModel.refreshRemoteData()
                    }
                )
            },
        ) { padding ->
            val selectedTab = remember { mutableStateOf(OverviewTabs.Overview) }
            val tabIndex = remember { mutableStateOf(0) }
            val tabs = listOf(
                OverviewTabs.Overview,
                OverviewTabs.Transactions,
                OverviewTabs.Categories,
                OverviewTabs.Tags,
            )
            val density = LocalDensity.current
            val areTabsVisible = remember {
                mutableStateOf(true)
            }
            val tabVisible = { tabVisible: Boolean ->
                areTabsVisible.value = tabVisible
            }
            LaunchedEffect(areTabsVisible.value) {
                if (!sheetManuallyHidden) {
                    if (areTabsVisible.value) {
                        modalSheetState.bottomSheetState.partialExpand()
                    } else {
                        modalSheetState.bottomSheetState.hide()
                    }
                }
            }
            val tabRowHeight = remember {
                mutableStateOf(0.dp)
            }
            val setExpenseTypes = { types: List<ExpenseType> ->
                accountOverviewViewModel.setExpenseTypes(types)
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (selectedTab.value) {
                    OverviewTabs.Overview ->
                        AccountOverview(
                            accountId = accountId,
                            chartDataList = chartDataList,
                            topExpenseList = topExpenseList,
                            chartData = chartData,
                            spendingByMonth = spendingByMonth,
                            cashFlowData = cashFlowData,
                            tabVisible = tabVisible,
                            tabRowHeight = tabRowHeight.value,
                            dateRange = selectedDateRange.value,
                            onAccountCashFlowDetailsClick = accountCashFlowDetailsClickInternal
                        )

                    OverviewTabs.Transactions ->
                        AccountTransactions(
                            itemList = transactionItems.value,
                            accountId = accountId,
                            tabVisible = tabVisible,
                            tabRowHeight = tabRowHeight.value
                        )

                    OverviewTabs.Categories -> AccountPieChart(
                        accountId = accountId,
                        activeItem = activeCategory.value,
                        itemSumMap = categorySum.value,
                        transactionsForActiveItem = transactionsForActiveCategory.value,
                        setActiveItem = setActiveCategory,
                        tabVisible = tabVisible,
                        expenseTypeUpdated = setExpenseTypes,
                        tabRowHeight = tabRowHeight.value
                    )

                    OverviewTabs.Tags -> AccountPieChart(
                        accountId = accountId,
                        activeItem = activeTag.value,
                        itemSumMap = tagSumForAccount.value,
                        transactionsForActiveItem = transactionForActiveTag.value,
                        setActiveItem = setActiveTag,
                        tabVisible = tabVisible,
                        expenseTypeUpdated = setExpenseTypes,
                        tabRowHeight = tabRowHeight.value
                    )
                }
                AnimatedVisibility(
                    visible = areTabsVisible.value,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    ScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .onGloballyPositioned { layoutCoordinates ->
                                tabRowHeight.value =
                                    with(density) { layoutCoordinates.size.height.toDp() }
                            },
                        selectedTabIndex = tabIndex.value,
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(
                                text = { Text(tab.name) },
                                selected = tabIndex.value == index,
                                onClick = {
                                    tabIndex.value = index
                                    selectedTab.value = tabs[index]
                                },
                                selectedContentColor = HaloBlue,
                                unselectedContentColor = DarkGrey
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowTopBarWithCalendar(
    accountName: String,
    modalSheetState: BottomSheetScaffoldState,
    refreshClicked: () -> Unit,
    onBack: () -> Unit
) {
    val dropDownMenuExpanded = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    AppBarWithBackAndOptions(
        title = accountName,
        backClicked = onBack,
        actions = {
            TopAppBarActionButton(
                painterResource(resource = Res.drawable.calendar_month_outline),
                description = "Select Range"
            ) {
                coroutineScope.launch {
                    if (modalSheetState.bottomSheetState.currentValue != SheetValue.Expanded) {
                        modalSheetState.bottomSheetState.expand()
                    } else {
                        modalSheetState.bottomSheetState.partialExpand()
                    }
                }
            }
//            TopAppBarActionButton(
//                painterResource(resource = Res.drawable.filter_menu),
//                description = "Filter"
//            ) {
//            }
//            TopAppBarActionButton(
//                imageVector = Icons.Outlined.MoreVert,
//                description = "Options"
//            ) {
//                dropDownMenuExpanded.value = true
//            }
//
//            DropdownMenu(
//                expanded = dropDownMenuExpanded.value,
//                onDismissRequest = {
//                    dropDownMenuExpanded.value = false
//                },
//                offset = DpOffset(x = 10.dp, y = (-60).dp)
//            ) {
//                DropdownMenuItem(
//                    text = {
//                        Text("Refresh")
//                    },
//                    onClick = {
////                        Toast.makeText(context, "Refresh Click", Toast.LENGTH_SHORT)
////                            .show()
//                        dropDownMenuExpanded.value = false
//                    })
//
//                DropdownMenuItem(
//                    text = {
//                        Text("Settings")
//                    },
//                    onClick = {
////                        Toast.makeText(context, "Settings Click", Toast.LENGTH_SHORT)
////                            .show()
//                        dropDownMenuExpanded.value = false
//                    })
//            }
        }
    )

}

@Composable
fun AccountPieChart(
    accountId: String,
    activeItem: String,
    itemSumMap: Map<String, TotalsSummary>,
    transactionsForActiveItem: List<FireFlyTransaction>,
    setActiveItem: (String) -> Unit,
    tabVisible: (visible: Boolean) -> Unit,
    expenseTypeUpdated: (types: List<ExpenseType>) -> Unit,
    tabRowHeight: Dp
) {
    LaunchedEffect(itemSumMap.entries) {
        if (activeItem.isEmpty() && itemSumMap.isNotEmpty()) {
            setActiveItem.invoke(itemSumMap.entries.first().key)
        }
    }
    val listState = rememberLazyListState()
    val isScrollingUp = listState.isScrollingUp()
    tabVisible.invoke(isScrollingUp)
    val dimensions = FireflyAppTheme.dimensions
    val tagTotal = remember {
        mutableDoubleStateOf(0.0)
    }
    val screenWidth = screenWidth()
    log.d { "screenWidth-$screenWidth" }
    var expenseSelected by remember { mutableStateOf(true) }
    var incomeSelected by remember { mutableStateOf(true) }
    var transferSelected by remember { mutableStateOf(true) }
    val expenseTypesSelected = remember {
        mutableStateOf(setOf(ExpenseType.EXPENSE, ExpenseType.INCOME, ExpenseType.TRANSFER))
    }
    LaunchedEffect(Unit) {
        // reset these values at every launch
        expenseTypeUpdated.invoke(expenseTypesSelected.value.toMutableList())
    }
    LaunchedEffect(expenseSelected, incomeSelected, transferSelected) {
        val set = mutableSetOf<ExpenseType>()
        if (expenseSelected) {
            set.add(ExpenseType.EXPENSE)
        }
        if (incomeSelected) {
            set.add(ExpenseType.INCOME)
        }
        if (transferSelected) {
            set.add(ExpenseType.TRANSFER)
        }
        expenseTypesSelected.value = set
    }
    val selectedTypes = remember(expenseTypesSelected) {
        mutableStateOf(listOf<ExpenseType>())
    }
    LaunchedEffect(expenseTypesSelected.value) {
        selectedTypes.value = expenseTypesSelected.value.toMutableList()
        expenseTypeUpdated.invoke(selectedTypes.value)
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(tabRowHeight))
        }
        item(key = "typeFilter") {
            FlowRow {
                Spacer(modifier = Modifier.width(5.dp))
                FilterChip(
                    onClick = {
                        if (incomeSelected || transferSelected) {
                            expenseSelected = !expenseSelected
                        }
                    },
                    label = {
                        Text(ExpenseType.EXPENSE.name)
                    },
                    selected = expenseSelected,
                    leadingIcon = if (expenseSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                Spacer(modifier = Modifier.width(5.dp))
                FilterChip(
                    onClick = {
                        if (expenseSelected || transferSelected) {
                            incomeSelected = !incomeSelected
                        }
                    },
                    label = {
                        Text(ExpenseType.INCOME.name)
                    },
                    selected = incomeSelected,
                    leadingIcon = if (incomeSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                Spacer(modifier = Modifier.width(5.dp))
                FilterChip(
                    onClick = {
                        if (expenseSelected || incomeSelected) {
                            transferSelected = !transferSelected
                        }
                    },
                    label = {
                        Text(ExpenseType.TRANSFER.name)
                    },
                    selected = transferSelected,
                    leadingIcon = if (transferSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
        item(key = "pieChart") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.contentMargin)
                    .wrapContentHeight()
            ) {
                PieChart(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .widthIn(0.dp, 480.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    pieEntriesMap = itemSumMap.map {
                        it.key to it.value.let { totals ->
                            totals.incomeSum + totals.expenseSum + totals.transferSum
                        }
                    }.toMap(),
                    setActiveTag = setActiveItem,
                    tagTotal = tagTotal,
                    types = selectedTypes.value
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = dimensions.verticalPadding,
                        horizontal = dimensions.contentMargin
                    ),
                text = activeItem,
                style = FireflyAppTheme.typography.bodyLarge,
                maxLines = 2
            )
            if (tagTotal.value > 0.0) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = dimensions.verticalPadding,
                            horizontal = dimensions.contentMargin
                        ),
                    text = "Total: ${tagTotal.value.getDisplayWithCurrency("$")}",
                    style = FireflyAppTheme.typography.bodyLarge
                        .copy(color = tagTotal.value.getTextColorForAmount()),
                    maxLines = 2
                )
            }
        }
        items(items = transactionsForActiveItem,
            key = {
                it.transaction_journal_id
            },
            contentType = {
                "transaction"
            }
        ) { item ->
            TransactionItem(
                modifier = Modifier.padding(horizontal = dimensions.contentMargin),
                accountId = accountId,
                item = item
            )
        }
        item(key = "bottomSpace") {
            Spacer(modifier = Modifier.height(tabRowHeight.times(2)))
        }
    }
}

@Composable
fun AccountTransactions(
    itemList: List<FireFlyTransaction>,
    accountId: String,
    tabVisible: (visible: Boolean) -> Unit,
    tabRowHeight: Dp
) {
    val dimensions = FireflyAppTheme.dimensions
    val listState = rememberLazyListState()
    val isScrollingUp = listState.isScrollingUp()
    LaunchedEffect(isScrollingUp) {
        tabVisible.invoke(isScrollingUp)
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(all = dimensions.contentMargin)
    ) {
        item {
            Spacer(modifier = Modifier.height(tabRowHeight))
        }
        items(itemList) { item ->
            TransactionItem(
                accountId = accountId,
                item = item
            )

        }
        item {
            Spacer(modifier = Modifier.height(dimensions.listSpacing))
        }

        item(key = "bottomSpacer") { Spacer(modifier = Modifier.height(BottomSheetDefaults.SheetPeekHeight)) }
    }
}