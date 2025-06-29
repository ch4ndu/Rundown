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

import Foundation
import UIKit

public struct AttributedStringUtil {
    public static func createAttributedString(
        date: String,
        expenseAmount: Float,
        incomeAmount: Float
    ) -> NSAttributedString {
        let attributedString = NSMutableAttributedString(string: date)

        if expenseAmount > 0 {
            let expenseText = "\nExpense: $\(String(format: "%.2f", expenseAmount))"
            let expenseAttributes: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.red
            ]
            let expenseAttributedString = NSAttributedString(string: expenseText, attributes: expenseAttributes)
            attributedString.append(expenseAttributedString)
        }

        if incomeAmount > 0 {
            let incomeText = "\nIncome: $\(String(format: "%.2f", incomeAmount))"
            let incomeAttributes: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.green
            ]
            let incomeAttributedString = NSAttributedString(string: incomeText, attributes: incomeAttributes)
            attributedString.append(incomeAttributedString)
        }

        return attributedString
    }
}
