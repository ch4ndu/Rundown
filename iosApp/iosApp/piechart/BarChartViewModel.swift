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
