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
