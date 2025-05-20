//
//  AttributedStringUtil.swift
//  iosApp
//
//  Created by Murali Vipparla on 3/25/25.
//  Copyright © 2025 orgName. All rights reserved.
//

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
