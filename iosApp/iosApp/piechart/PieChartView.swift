//
//  PieChartView.swift
//  iosApp
//
//  Created by Murali Vipparla on 7/26/24.
//  Copyright © 2024 orgName. All rights reserved.
//
import UIKit

import Foundation
import SwiftUI

struct PieChartView: View {
//    let pieEntriesMap: [String: Double]
//    let setActiveTag: (String) -> Void
    @ObservedObject var viewData: ObservableViewData
    
    var body: some View {
        VStack {
            Text("Got pieChart data with \(viewData.pieEntriesMap.count) values")
                .padding()
                .font(.system(size: 20))
        }
    }
}



//@objc public struct PieChartViewWrapper: UIViewControllerRepresentable {
//    let pieEntriesMap: [String: Double]
//    let setActiveTag: (String) -> Void
//    
//    @objc public func makeUIViewController(context: Context) -> UIViewController {
//        
////        let hostingController = UIHostingController(rootView: PieChartView(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag))
//        let hostingController = UIHostingController(rootView: PieChartView(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag))
//        return hostingController
//    }
//    
//    @objc public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
//        // No need to update anything here
//    }
//}
