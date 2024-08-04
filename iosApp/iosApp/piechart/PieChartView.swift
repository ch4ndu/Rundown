//
//  PieChartView.swift
//  iosApp
//
//  Created by Murali Vipparla on 7/26/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import UIKit

import Foundation
import SwiftUI
import Charts

struct PieSegment: Identifiable {
    let id = UUID()
    let name: String
    let value: Double
}

//struct NewPieChartView: View {
struct PieChartView: View {
    @ObservedObject var viewData: ObservableViewData
    let data: [PieSegment] = [
        PieSegment(name: "Apples", value: 40),
        PieSegment(name: "Bananas", value: 30),
        PieSegment(name: "Cherries", value: 20),
        PieSegment(name: "Dates", value: 100)
    ]
    
    var body: some View {
        if #available(iOS 17.0, *) {
            Chart {
                ForEach(data) { segment in
                    SectorMark(
                        angle: .value("Value", segment.value),
                        angularInset: 1
                    )
                    .foregroundStyle(by: .value("Name", segment.name))
                }
            }
            .chartLegend(.visible)
            .frame(height: 300)
        } else {
            // Fallback on earlier versions
                VStack {
                    Text("Unsupported version")
                        .padding()
                        .font(.system(size: 20))
                }
        }
    }
}
