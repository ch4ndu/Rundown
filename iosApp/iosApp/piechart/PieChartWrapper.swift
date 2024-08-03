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
//    var pieEntriesMap: [String: Double]
//    var setActiveTag: (String) -> Void
    private var viewData: ObservableViewData
    var controller: UIViewController? = nil
    
    @objc override init() {
        self.viewData = ObservableViewData(pieEntriesMap: [:], setActiveTag: { (test) -> () in
            
        })
//        self.pieEntriesMap = [:]
//        self.setActiveTag = { (test) -> () in }
    }

    @objc init(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void ) {
        self.viewData = ObservableViewData(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag)
//        self.pieEntriesMap = pieEntriesMap
//        self.setActiveTag = setActiveTag
    }

    @objc public func makeViewController() -> UIViewController {
        if (controller == nil) {
            controller = UIHostingController(rootView: PieChartView(viewData: viewData))
//            controller = PieChartViewController()
        }
        
        return controller ?? UIViewController()
    }
    
    @objc public func getView() -> UIView {
        return makeViewController().view
    }
    
    @objc public func updateData(pieEntriesMap: [String: Double],setActiveTag: @escaping (String) -> Void ) {
        self.viewData.pieEntriesMap = pieEntriesMap
        self.viewData.setActiveTag = setActiveTag
    }
}
