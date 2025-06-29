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

package app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.theme.GreenAlpha
import app.theme.WalletRedAlpha
import app.theme.WalletTealAlpha
import data.database.model.transaction.FireFlyTransaction
import data.database.serializers.DateSerializer
import kotlinx.datetime.format
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lighthousegames.logging.logging

private val log = logging()

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    accountId: String,
    item: FireFlyTransaction
) {
    ThemedCard(modifier = modifier, onClick = {
        log.d { "accountId:$accountId" }
        log.d { "transaction: $item" }
    }) {
        val adjustedAmount = item.displayWithCurrency
        val amountTextColor = item.amountTextColor
//            if (accountId == item.source_id?.toString())
//                item.amountTextColorForSameSource
//            else
//                item.amountTextColor

        val dimensions = FireflyAppTheme.dimensions
        val greenColor = GreenAlpha
        val redColor = WalletRedAlpha
        val tealColor = WalletTealAlpha
        val roundShape = FireflyAppTheme.shapes.large
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensions.horizontalPadding,
                    vertical = dimensions.verticalPadding
                )
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(vertical = 3.dp)) {
                Text(
                    text = item.description,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start)
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = FireflyAppTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = dimensions.childPadding),
                    text = adjustedAmount,
                    style = FireflyAppTheme.typography.bodyLarge
                        .copy(color = amountTextColor),
                    textAlign = TextAlign.End
                )

            }
            if (accountId.isEmpty() || accountId == "0" || accountId != item.source_id?.toString()) {
                val sourceName = item.source_name
                if (sourceName?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Origin: $sourceName",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = dimensions.childPadding
                            ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = FireflyAppTheme.typography.bodyMedium
                    )
                }
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensions.childPadding,
                        bottom = dimensions.childPadding
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .padding(
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .align(Alignment.CenterVertically),
                    text = item.date
                        .format(DateSerializer.transactionDisplayFormat),
                    style = FireflyAppTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .background(
                            color = tealColor,
                            shape = roundShape
                        )
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .align(Alignment.CenterVertically),
                    text = item.transactionType,
                    maxLines = 1,
                    style = FireflyAppTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )
                val category = item.category_name
                if (category?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .background(
                                color = redColor,
                                shape = roundShape
                            )
                            .padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 4.dp,
                                bottom = 4.dp
                            )
                            .align(Alignment.CenterVertically),
                        text = category,
                        maxLines = 1,
                        style = FireflyAppTheme.typography.labelLarge,
                        overflow = TextOverflow.Ellipsis
                    )
                }
//                if (item.tags.isNotEmpty()) {
//                    item.tags.forEach { tag ->
//                        Spacer(modifier = Modifier.width(3.dp))
//                        Text(
//                            text = tag.tag,
//                            modifier = Modifier
//                                .padding(vertical = 5.dp)
//                                .background(
//                                    color = greenColor,
//                                    shape = roundShape
//                                )
//                                .padding(
//                                    start = 8.dp,
//                                    end = 8.dp,
//                                    top = 4.dp,
//                                    bottom = 4.dp
//                                )
//                                .align(Alignment.CenterVertically),
//                            maxLines = 1,
//                            style = FireflyAppTheme.typography.labelLarge,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                    }
//                }
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    FireflyAppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.wrapContentSize(),
//            color = MaterialTheme.colorScheme.background
        ) {
            TransactionItem(accountId = "1", item = FireFlyTransaction.getMockTransaction())
        }
    }
}