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
    
    
    @objc public func update(expenseIncomeData: [String: [Float]], dateSelected: @escaping (String) -> Void) {
        var barDataList: [BarChartData] = []
        print("got data: size:\(expenseIncomeData.count)")
        print("got data: keys:\(expenseIncomeData.keys)")
        print("got data: values:\(expenseIncomeData.values)")
        expenseIncomeData.keys.forEach { dateString in
            barDataList.append(BarChartData(dateString: dateString, expenseAmount: expenseIncomeData[dateString]?[0] ?? 0, incomeAmount: (expenseIncomeData[dateString]?[1] ?? 0)))
        }
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
