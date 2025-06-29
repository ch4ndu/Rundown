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
import UIKit

import Foundation
import SwiftUI
import Charts

struct PieSegment: Identifiable {
    let id = UUID()
    let name: String
    let value: Double
}

struct PieChartView: View {
    @ObservedObject var viewData: ObservableViewData
    @State var selectedSegment: PieSegment? = nil
    @State private var selectedAngle: Double? = nil
    
    var body: some View {
        if #available(iOS 17.0, *) {
            ZStack {
                Chart {
                    ForEach(viewData.pieSegments) { segment in
                        SectorMark(
                            angle: .value("Value", segment.value),
                            //                            innerRadius: .ratio(selectedSegment?.name == segment.name ? 0.5 : 0.6),
                            outerRadius: .ratio(selectedSegment?.name == segment.name ? 1.0 : 0.9),
                            angularInset: 1.0
                        )
                        .foregroundStyle(by: .value("Name", segment.name))
                        //                        .cornerRadius(6.0)
                        .opacity(selectedSegment?.name == segment.name ? 1.0 : 0.8)
                    }
                }
                .chartAngleSelection(value: $selectedAngle)
                .chartLegend(.hidden)
                .background(Color.clear)
                .onChange(of: selectedAngle) { oldValue, newValue in
                    if let newValue {
                        selectedSegment = findSelectedSegment(value: newValue)
                        if let selectedSegment {
                            viewData.setActiveTag(selectedSegment.name)
                        }
                    }
                }
            }
        } else {
            // Fallback on earlier versions
            VStack {
                Text("Unsupported version")
                    .padding()
                    .font(.system(size: 20))
            }
        }
    }
    private func findSelectedSegment(value: Double) -> PieSegment? {
        
        var accumulatedCount = 0.0
        
        let temp = viewData.pieSegments.first { (segment) in
            accumulatedCount += segment.value
            return value <= accumulatedCount
        }
        
        return temp
    }
}
