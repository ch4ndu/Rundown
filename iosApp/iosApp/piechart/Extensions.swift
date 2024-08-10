//
//  Extensions.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/10/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

extension DateFormatter {
    // Custom initializer that accepts a date format string
    convenience init(dateFormat: String) {
        self.init()  // Call the default initializer
        self.dateFormat = dateFormat
    }
}


let dateFormatter = DateFormatter(dateFormat: "MMM yyyy")

extension NSDate {
    
    func formatDate() -> String {
        return dateFormatter.string(from: self as Date)
    }
}
