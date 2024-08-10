//
//  BarChartViewModel.swift
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


class BarChartViewModel: ObservableObject {
    @Published var data: [BarDataPoint] = [
        BarDataPoint(category: "A", value: 10),
        BarDataPoint(category: "B", value: 20),
        BarDataPoint(category: "C", value: 30),
        BarDataPoint(category: "D", value: 40)
    ]
    @Published var selectedDataPoint: BarDataPoint? = nil
}
