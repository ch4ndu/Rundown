//
//  MessageView.swift
//  iosApp
//
//  Created by Murali Vipparla on 7/30/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct MessageView: View {
    let message: String
    var body: some View {
        Text("Fucking message")
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .cornerRadius(10)
    }
}
