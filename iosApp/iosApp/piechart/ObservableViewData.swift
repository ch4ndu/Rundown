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
    @Published var setActiveTag: (String) -> Void

    init(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void) {
        self.pieEntriesMap = pieEntriesMap
        self.setActiveTag = setActiveTag
    }
}
