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
import SwiftUI

@objc public class PieChartWrapper: NSObject {
    private var viewData: ObservableViewData
    var controller: UIViewController? = nil
    
    @objc override init() {
        self.viewData = ObservableViewData(pieEntriesMap: [:], setActiveTag: { (test) -> () in
            
        }, pieSegments: [])
    }

    @objc init(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void ) {
        let temp: [PieSegment] = pieEntriesMap.map { (key, value) in
            PieSegment(name: key, value: value)
        }
        self.viewData = ObservableViewData(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag, pieSegments: temp)
    }

    @objc public func makeViewController() -> UIViewController {
        if (controller == nil) {
            controller = UIHostingController(rootView: PieChartView(viewData: viewData))
            //TODO set background color and text color from compose
//            controller?.view?.backgroundColor = UIColor.clear
        }
        
        return controller ?? UIViewController()
    }
    
    @objc public func getView() -> UIView {
        return makeViewController().view
    }
    
    @objc public func updateData(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void ) {
        self.viewData.pieEntriesMap = pieEntriesMap
        self.viewData.setActiveTag = setActiveTag
        self.viewData.pieSegments = pieEntriesMap.map { (key, value) in
            PieSegment(name: key, value: value)
        }.sorted(by: { left, right in
            left.value < right.value
        })
    }
}
