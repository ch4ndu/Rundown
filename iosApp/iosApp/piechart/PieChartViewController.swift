//
//  PieChartViewController.swift
//  iosApp
//
//  Created by Murali Vipparla on 8/2/24.
//  Copyright © 2024 orgName. All rights reserved.
//
//import UIKit
//import DGCharts
//
//class PieChartViewController: UIViewController, ChartViewDelegate {
//
//    var pieChartView: PieChartView!
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        
//        // Create a PieChartView instance
//        pieChartView = PieChartView()
//        
//        // Set the frame (or use Auto Layout)
//        pieChartView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
//        pieChartView.center = view.center
//        
//        // Set the delegate
//        pieChartView.delegate = self
//        
//        // Add pieChartView to the view hierarchy
//        view.addSubview(pieChartView)
//        
//        // Set data for the PieChart
//        setChartData()
//    }
//
//    func setChartData() {
//        // Example data entries
//        let entries = [
//            PieChartDataEntry(value: 40, label: "A"),
//            PieChartDataEntry(value: 30, label: "B"),
//            PieChartDataEntry(value: 20, label: "C"),
//            PieChartDataEntry(value: 10, label: "D")
//        ]
//        
//        let dataSet = PieChartDataSet(entries: entries, label: "Sample Data")
//        
//        // Set colors (optional)
//        dataSet.colors = ChartColorTemplates.material()
//        
//        // Set chart data
//        let data = PieChartData(dataSet: dataSet)
//        pieChartView.data = data
//        
//        // Additional customizations (optional)
//        pieChartView.legend.enabled = true
//        pieChartView.chartDescription?.enabled = false
//        pieChartView.holeColor = UIColor.clear
//    }
//
//    // ChartViewDelegate method
//    func chartValueSelected(_ chartView: ChartViewBase, entry: ChartDataEntry, highlight: Highlight) {
//        guard let pieEntry = entry as? PieChartDataEntry else { return }
//        let value = pieEntry.value
//        let label = pieEntry.label ?? "Unknown"
//        
//        print("Selected slice: \(label) with value \(value)")
//        
//        // Example action: display an alert
//        let alert = UIAlertController(title: "Slice Selected", message: "You selected \(label) with value \(value)", preferredStyle: .alert)
//        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
//        present(alert, animated: true, completion: nil)
//    }
//}
