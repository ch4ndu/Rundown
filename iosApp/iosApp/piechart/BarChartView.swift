//
//  BarchartView.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/4/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import Combine
import SwiftUI
import Charts
import UIKit

struct BarChartData: Identifiable {
    let id = UUID()
    let date: NSDate
    let expenseAmount: Float
    let incomeAmount: Float
}

struct BarChartView: View {
    @ObservedObject var observableBarData = ObservableBarData()
    
    var body: some View {
        if #available(iOS 17.0, *) {
            VStack {
                createChart()
                //                createAnnotation()
            }
            .padding()
            .background(Color.clear)
        } else {
            VStack {
                Text("Not Implemented")
            }
        }
    }
    
    // Helper function to create the chart
    @available(iOS 17.0, *)
    @ViewBuilder
    private func createChart() -> some View {
        Chart(observableBarData.barchartData) { barData in
            let dateString = barData.date.formatDate()
            let amount = Double(barData.expenseAmount)
            // Bar for each DataPoint
            BarMark(
                x: .value("Date", dateString),
                y: .value("Amount", amount)
            )
            .foregroundStyle(barData.id == observableBarData.selectedBarData?.id ? .orange : .blue)
            .clipShape(RoundedRectangle(cornerRadius: 20))
            
            if (barData.id == observableBarData.selectedBarData?.id) {
                RuleMark(x: .value("Date", dateString))
                    .lineStyle(StrokeStyle(lineWidth: 2, dash: [5, 5]))
                    .foregroundStyle(Color.gray)
                PointMark(
                    x: .value("Date", dateString),
                    y: .value("Amount", amount)
                ).symbol {
                    VStack{
                        Text("Date: \(dateString) ")
                            .font(.caption)
                            .padding(0)
                            .background(Color.clear)
                        Text("$\(amount, specifier: "%.1f") ")
                            .font(.caption)
                            .padding(0)
                            .background(Color.clear)
                    }
                    .background(Color.white)
                    .shadow(radius: 5)
                    .cornerRadius(5)
                    .padding(5)
                }
                .offset(y: -15) // Adjust the position above the bar
            }
        }
        .chartYAxis {
            AxisMarks(preset: .extended, position: .leading, values: .automatic(desiredCount: 3))
        }
        .chartXAxis {
            AxisMarks(position: .bottom, values: .automatic(minimumStride: 5)) { value in
                AxisValueLabel() {
                    if let stringValue = value.as(String.self) {
                        Text(stringValue)
                            .font(.caption)
                            .rotationEffect(.degrees(-45)) // Rotate the text
                            .offset(x: -10, y: 0) // Adjust the offset if necessary
                    }
                }
            }
            //        }
            //            AxisMarks(preset: .automatic, position: .bottom, values: .automatic(minimumStride: 2,desiredCount: 2))
        }
        .chartYScale(domain: 0...observableBarData.maxValue())
        .chartOverlay(content: { proxy in
            GeometryReader(content: { geometry in
                ZStack(alignment: .top, content: {
                    Rectangle()
                        .fill(Color.clear)
                        .contentShape(Rectangle())
                        .gesture(
                            DragGesture(minimumDistance: 0)
                                .onChanged({ value in
                                    let location = value.location
                                    observableBarData.updateSelectedMonth(at: location, proxy: proxy, geometry: geometry)
                                })
                        )
                })
            })
        })
        .background(Color.clear)
    }
    
    // Helper function to create the annotation
    //    @ViewBuilder
    //    private func createAnnotation() -> some View {
    //        if let selected = observableBarData.selectedBarData {
    //            Text("\(selected.expenseAmount, specifier: "%.1f")")
    //                .font(.caption)
    //                .padding(4)
    //                .background(Color.white)
    //                .cornerRadius(5)
    //                .shadow(radius: 5)
    //                .offset(y: 0)
    //                .transition(.opacity)
    //        }
    //    }
}
