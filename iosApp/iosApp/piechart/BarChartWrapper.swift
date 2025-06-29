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
import SwiftUI

@objc public class BarChartWrapper : NSObject {
    private var controller: UIViewController? = nil
    private var observableBarChartData: ObservableBarData
    
    
    @objc public func update(expenseIncomeData: [NSDate: [Float]], dateSelected: @escaping (NSDate) -> Void) {
        var barDataList: [BarChartData] = []
        print("got data: size:\(expenseIncomeData.count)")
        print("got data: keys:\(expenseIncomeData.keys)")
        print("got data: values:\(expenseIncomeData.values)")
        expenseIncomeData.keys.forEach { date in
            barDataList.append(BarChartData(date: date, expenseAmount: expenseIncomeData[date]?[0] ?? 0, incomeAmount: (expenseIncomeData[date]?[1] ?? 0)))
        }
        barDataList.sort(by: {$0.date.compare($1.date as Date) == .orderedDescending})
        self.observableBarChartData.barchartData = barDataList
        self.observableBarChartData.dateSelected = dateSelected
    }
    
    @objc init(controller: UIViewController? = nil) {
        self.controller = controller
        self.observableBarChartData = ObservableBarData(barchartData: [], dateSelected: { dateSelected in
            
        })
    }
    
    override public init() {
        self.observableBarChartData = ObservableBarData(barchartData: [], dateSelected: { dateSelected in
            
        })
    }
    @objc public func makeViewController() -> UIViewController {
        if (controller == nil) {
            controller = UIHostingController(
                rootView: BarChartView(observableBarData: observableBarChartData)
            )
//            controller?.view?.backgroundColor = UIColor.clear
        }
        
        return controller ?? UIViewController()
    }
    
    @objc public func getView() -> UIView {
        return makeViewController().view
    }
}
