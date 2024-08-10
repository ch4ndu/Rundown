//
//  BarDataPoint.swift
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

struct BarDataPoint: Identifiable {
    var id = UUID()
    var category: String
    var value: Double
}
