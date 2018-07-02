//
//  ViewController.swift
//  LINELoginDemo
//
//  Created by KuanWei on 2018/4/24.
//  Copyright © 2018年 Kuan-Wei. All rights reserved.
//

import UIKit
import LineSDK

class ViewController: UIViewController, LineSDKLoginDelegate {

    let apiClient = LineSDKAPI(configuration: LineSDKConfiguration.defaultConfig())

    override func viewDidLoad() {
        super.viewDidLoad()

        LineSDKLogin.sharedInstance().delegate = self
    }

    func didLogin(_ login: LineSDKLogin, credential: LineSDKCredential?, profile: LineSDKProfile?, error: Error?) {

        if (error != nil) {
            print("error: \(String(describing: error?.localizedDescription))")
        }
        else {
            let accessToken = credential?.accessToken?.accessToken
            print(accessToken)
            let userID = profile?.userID
            print(userID)
            let displayName = profile?.displayName
            print(displayName)
            let statusMessage = profile?.statusMessage
            print(statusMessage)
            let pictureURL = profile?.pictureURL

            let pictureUrlString: String
            if (pictureURL != nil) {
                pictureUrlString = (profile?.pictureURL?.absoluteString)!
                print(pictureUrlString)
            }
        }
    }

    @IBAction func onLoginAction(_ sender: UIButton) {
        LineSDKLogin.sharedInstance().start()
    }
    
    @IBAction func onLogoutAction(_ sender: UIButton) {

        apiClient.logout(queue: DispatchQueue.main) { (success, error) in
            if success {
                print("success")
            }else{
                print("failed error = \(error?.localizedDescription)")
            }
        }
    }
    
}

