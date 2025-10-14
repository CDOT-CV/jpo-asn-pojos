import os
import re
import random

def update_axles(path):
    """
    Update axle-related values in JSON files within a directory tree.
    
    This function recursively walks through a directory tree and updates
    totalAxles, frontAxles, and rearAxles values in all JSON files found.
    Each field is replaced with a random integer between 1 and 10.
    
    Args:
        path (str): The root directory path to start the recursive search.
                    Can be relative (e.g., ".") or absolute path.
    
    Returns:
        None
    
    Side Effects:
        - Modifies JSON files in-place by updating axle values
        - Prints the path of each updated file to stdout
    
    Note:
        The function uses regex patterns to find and replace axle values,
        so it will update ALL occurrences of these fields in each JSON file.
    """
    for root, _, files in os.walk(path):
        for file in files:
            if file.endswith(".json"):
                full_path = os.path.join(root, file)
                with open(full_path, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Generate a random value for each field
                total_axles_value = random.randint(1, 10)
                front_axles_value = random.randint(1, 10)
                rear_axles_value = random.randint(1, 10)

                # Replace values using regex - replace ALL occurrences (count=0 means replace all)
                # Use -?\d+ to match both positive and negative integers
                content = re.sub(r'("totalAxles"\s*:\s*)-?\d+', r'\g<1>' + str(total_axles_value), content, count=0)
                content = re.sub(r'("frontAxles"\s*:\s*)-?\d+', r'\g<1>' + str(front_axles_value), content, count=0)
                content = re.sub(r'("rearAxles"\s*:\s*)-?\d+', r'\g<1>' + str(rear_axles_value), content, count=0)

                with open(full_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f"Updated: {full_path}")

# Example usage - now handles all subfolders recursively
update_axles(".") 