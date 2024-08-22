//
//  ObservableBarData.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/9/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation

import Foundation
import SwiftUI
import Charts

class ObservableBarData: ObservableObject {
    @Published var barchartData: [BarChartData]
    @Published var dateSelected: (NSDate) -> Void
    
    @Published var selectedBarData: BarChartData? = nil
    
    init(barchartData: [BarChartData], dateSelected: @escaping (NSDate) -> Void) {
        self.barchartData = barchartData
        self.dateSelected = dateSelected
    }
    
    init() {
        self.barchartData = []
        self.dateSelected = { (dateSelected) in
            
        }
    }
    
    @available(iOS 16.0, *)
    func updateSelectedMonth(at location: CGPoint, proxy: ChartProxy, geometry: GeometryProxy) {
        let xPosition = location.x - geometry[proxy.plotAreaFrame].origin.x
        if let dateString: String = proxy.value(atX: xPosition) {
            selectedBarData = barchartData.first { $0.date.formatDate() == dateString }
        }
        if let barData = selectedBarData {
            dateSelected(barData.date)
        }
    }
    
    func maxPointValue() -> Float {
        let max = barchartData.map { $0.expenseAmount + $0.incomeAmount }.max() ?? 0
        if( max <= 100) {
            return 400
        } else {
            return max
        }
    }
    
    func calcMaxValue() -> Float {
        let max = barchartData.map { $0.expenseAmount + $0.incomeAmount }.max() ?? 0
        if( max <= 100) {
            return 400
        } else {
            return max + max/10
        }
    }
}
