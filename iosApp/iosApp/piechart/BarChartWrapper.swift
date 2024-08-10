//
//  BarChartWrapper.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/6/24.
//  Copyright © 2024 orgName. All rights reserved.
//

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
