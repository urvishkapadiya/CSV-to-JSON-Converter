# CSV-to-JSON-Converter

Welcome to CSV2JSON, a Java consol based tool designed to transform Excel spreadsheet data into JSON format. This project was developed to address the challenges faced by a car rental company, which relied on Excel sheets for their daily operations. With CSV2JSON, you can efficiently convert and store your data using a cloud server and a NoSQL database, enhancing data integrity, security, and accessibility.

Project Overview
CSV2JSON enables you to convert CSV files, commonly used for tabular data, into JSON format. This conversion allows for better data management, analysis, and sharing among employees. Whether it's rental records or car maintenance history, CSV2JSON handles the transformation seamlessly.

Features
Exception Handling: CSV2JSON incorporates robust exception handling to ensure smooth data conversion. Invalid data, missing attributes, and incomplete records are handled gracefully, providing informative error messages.

Flexible Attribute Handling: CSV2JSON is designed to accommodate various attributes in your CSV files. It adapts to changes in attribute names while maintaining the data's integrity.

Comprehensive Logging: Any issues encountered during the conversion process are logged for reference. This feature helps in tracking and resolving data inconsistencies.

User-Friendly Interface: The program interacts with users through clear and informative prompts. It guides users in providing valid input and selecting the desired JSON files for display.

How to Use
1. Run the program and provide the names of the input CSV files (e.g., "CarRentalRecords.csv", "CarMaintenanceRecords.csv").

2. CSV2JSON will validate the files and ensure their correctness. If a file has missing attributes, it won't be converted, and you'll receive an error message and a log entry.

3. The program will process the valid files, converting each record into a JSON object. Records with missing data will be logged, but the conversion process will continue.

4. Once the JSON files are created, you'll be prompted to enter the name of a JSON file for display.

5. The chosen JSON file will be read and displayed, giving you an insight into the transformed data.
