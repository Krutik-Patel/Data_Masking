import requests
import os

def get_mime_type(file_path):
    """
    Determine the MIME type based on the file extension.
    
    Args:
        file_path (str): Path to the file.
    
    Returns:
        str: MIME type of the file.
    """
    extension = os.path.splitext(file_path)[1].lower()
    if extension == ".xml":
        return "application/xml"
    elif extension == ".json":
        return "application/json"
    elif extension == ".csv":
        return "text/csv"
    else:
        return "application/octet-stream"  # Default for unknown types

def upload_files_to_api(data_file_path, config_file_path, api_url="http://localhost:8080/processData"):
    """
    Uploads data and config files to the API and retrieves the masked output.
    
    Args:
        data_file_path (str): Path to the data file (e.g., XML or JSON).
        config_file_path (str): Path to the config file (e.g., XML or JSON).
        api_url (str): URL of the API endpoint.
    
    Returns:
        dict: API response containing message and masked output (or error).
    """
    try:
        # Determine MIME types based on file extensions
        data_mime_type = get_mime_type(data_file_path)
        config_mime_type = get_mime_type(config_file_path)

        # Prepare the files for the multipart/form-data request
        files = {
            "data": ("data" + os.path.splitext(data_file_path)[1], open(data_file_path, "rb"), data_mime_type),
            "config": ("config" + os.path.splitext(config_file_path)[1], open(config_file_path, "rb"), config_mime_type)
        }

        # Send POST request to the API
        response = requests.post(api_url, files=files)

        # Check if the request was successful
        response.raise_for_status()

        # Parse the JSON response
        return response.json()

    except requests.exceptions.RequestException as e:
        return {"error": f"Failed to connect to API: {str(e)}"}
    except ValueError as e:
        return {"error": f"Invalid response format: {str(e)}"}
    except FileNotFoundError as e:
        return {"error": f"File not found: {str(e)}"}
    finally:
        # Close file handles if they were opened
        for file in files.values():
            file[1].close()

def main():
    # Specify the paths to your local XML or JSON files
    data_file_path = "../example_config_and_engine_core_files/data_files/sample_data3.xml"  # Example XML data file
    config_file_path = "../example_config_and_engine_core_files/config_files/sample_config3.json"  # Example JSON config file

    # Call the function to upload files and get the response
    result = upload_files_to_api(data_file_path, config_file_path)

    # Process the response
    if "error" in result:
        print(f"Error: {result['error']}")
    else:
        print(f"Message: {result['message']}")
        print(f"Masked Output: {result['additionalText']}")

if __name__ == "__main__":
    main()