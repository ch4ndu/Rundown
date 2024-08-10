//
//  ObservableViewData.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/2/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import UIKit

import Foundation
import SwiftUI

class ObservableViewData: ObservableObject {
    @Published var pieEntriesMap: [String: Double]
    @Published var pieSegments: [PieSegment]
    @Published var setActiveTag: (String) -> Void
    
    var totalValue: Double {
        pieSegments.map { $0.value }.reduce(0, +)
    }

    init(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void, pieSegments: [PieSegment]) {
        self.pieEntriesMap = pieEntriesMap
        self.setActiveTag = setActiveTag
        self.pieSegments = pieSegments
    }
}
