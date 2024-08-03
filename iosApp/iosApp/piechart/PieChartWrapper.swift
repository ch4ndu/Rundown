//
//  PieChartWrapper.swift
//  iosApp
//
//  Created by Murali Vipparla on 7/30/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI

@objc public class PieChartWrapper: NSObject {
    let pieEntriesMap: [String: Double]
    let setActiveTag: (String) -> Void
    
    @objc override init() {
        self.pieEntriesMap = [:]
        self.setActiveTag = { }
    }

    @objc init(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void ) {
        self.pieEntriesMap = pieEntriesMap
        self.setActiveTag = setActiveTag
    }

    @objc public func makeViewController() -> UIViewController {
        return UIHostingController(rootView: PieChartView(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag))
    }
    
    @objc public func getView() -> UIView {
        return makeViewController().view
    }
}
