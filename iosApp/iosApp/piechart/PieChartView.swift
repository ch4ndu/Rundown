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
    let pieEntriesMap: [String: Double]
    let setActiveTag: (String) -> Void
    var body: some View {
            Text("Hello from SwiftUI!")
                .padding()
        }
}



struct PieChartViewWrapper: UIViewControllerRepresentable {
    let pieEntriesMap: [String: Double]
    let setActiveTag: (String) -> Void
    
    func makeUIViewController(context: Context) -> UIViewController {
        
//        let hostingController = UIHostingController(rootView: PieChartView(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag))
        let hostingController = UIHostingController(rootView: PieChartView(pieEntriesMap: pieEntriesMap, setActiveTag: setActiveTag))
        return hostingController
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No need to update anything here
    }
}
