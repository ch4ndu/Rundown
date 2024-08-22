@file:OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.AppBarWithOptions
import app.ui.BarChart
import app.ui.DetailsIconButton
import app.ui.Savers
import app.ui.ThemedBox
import app.ui.TopAppBarActionButton
import app.ui.getAspectRatio
import app.ui.getDisplayOverView
import app.ui.isLargeScreen
import app.viewmodel.CategoriesOverviewViewModel
import data.database.serializers.DateSerializer
import domain.atBeginningOfMonth
import domain.currentDate
import domain.minusMonths
import domain.model.DateRange
import domain.withEndOfMonthAtEndOfDay
import domain.withStartOfDay
import fireflycomposemultiplatform.composeapp.generated.resources.Res
import fireflycomposemultiplatform.composeapp.generated.resources.calendar_month_outline
import fireflycomposemultiplatform.composeapp.generated.resources.filter_menu
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun CategoriesOverviewScreen(
    categoriesOverviewViewModel: CategoriesOverviewViewModel = koinViewModel(),
    onCategoryDetailsClick: (category: String, dateRange: DateRange) -> Unit,
    onBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val dimensions = FireflyAppTheme.dimensions
    val categorySpending =
        categoriesOverviewViewModel.allCategoriesSpending.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
            confirmValueChange = { sheetValue ->
                log.d { "confirmValueChange:sheetValue:$sheetValue" }
                true
            }
        )
    )

    val selectedDateRange = rememberSaveable(saver = Savers.DateRange) {
        mutableStateOf(
            DateRange(
                currentDate().minusMonths(5).atBeginningOfMonth().withStartOfDay(),
                currentDate().withEndOfMonthAtEndOfDay()
            )
        )
    }
    LaunchedEffect(selectedDateRange.value) {
        categoriesOverviewViewModel.setDateRange(selectedDateRange.value)
    }
//    BackHandler(enabled = true) {
//        if (modalSheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
//            coroutineScope.launch {
//                modalSheetState.bottomSheetState.hide()
//            }
//        } else {
//            onBack.invoke()
//        }
//    }

    val isLargeScreen = isLargeScreen()
    val aspectRatio = remember(isLargeScreen.value) {
        getAspectRatio(isLargeScreen.value)
    }
    BottomSheetScaffold(
        scaffoldState = modalSheetState,
        sheetContent = {
            FilterScreen(
                dateRange = selectedDateRange.value,
                onClose = {
                    scope.launch { modalSheetState.bottomSheetState.hide() }
                },
                onSave = {
                    selectedDateRange.value = it
                    scope.launch {
                        modalSheetState.bottomSheetState.hide()
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
                ShowTopBar(modalSheetState = modalSheetState)
            },
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(dimensions.listSpacing),
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(all = dimensions.contentMargin)
                ) {
                    categorySpending.value.forEach { categorySpending ->
                        item { Spacer(modifier = Modifier.height(dimensions.listSpacing)) }
                        item(
                            key = categorySpending.categoryName,
                            contentType = "overview"
                        ) {
                            ThemedBox(
                                modifier = Modifier.fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                DetailsIconButton(
                                    boxScope = this,
                                ) {
                                    onCategoryDetailsClick.invoke(categorySpending.categoryName, selectedDateRange.value)
                                }
                                Column {
                                    Text(
                                        text = "Category: ${categorySpending.categoryName}",
                                        modifier = Modifier.padding(6.dp)
                                    )
                                    val overview = categorySpending.getDisplayOverView()
                                    if (overview.isNotEmpty()) {
                                        Text(
                                            text = overview,
                                            modifier = Modifier.padding(6.dp),
                                            maxLines = 3
                                        )

                                    }
                                    BarChart(
                                        dataList = categorySpending.expenseIncomeData,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(aspectRatio),
                                        dateTimeFormatter = DateSerializer.chartMonthYearFormat,
                                        dataSelected = {}
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(dimensions.listSpacing)) }
                }
            }
        }
    }
}

@Composable
private fun ShowTopBar(
    modalSheetState: BottomSheetScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()
    AppBarWithOptions(
        title = "Categories",
        actions = {
            TopAppBarActionButton(
                painterResource(resource = Res.drawable.calendar_month_outline),
                description = "Select Range"
            ) {
                coroutineScope.launch {
                    if (modalSheetState.bottomSheetState.currentValue != SheetValue.Expanded) {
                        modalSheetState.bottomSheetState.expand()
                    } else {
                        modalSheetState.bottomSheetState.hide()
                    }
                }
            }
//            TopAppBarActionButton(
//                painterResource(resource = Res.drawable.filter_menu),
//                description = "Filter"
//            ) {
//            }
        }
    )

}