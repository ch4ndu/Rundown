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
