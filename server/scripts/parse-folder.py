import os
import subprocess
import shutil

# files_to_parse = ["V75CWEUR", "V75CW88A", "V75CW880", "V75CW201", "V75CW129", "V75CW124", "V75CW123",
#                   "V75CW022", "V75CW013", "V75CW002", "V75CP88A", "V75CP202", "V75CP201", "V75CP129",
#                   "V75CP124", "V75CP123", "V75CP011", "V7523438-compiled", "V751C931", "V7588049"]
files_to_parse = ["V75CWEUR"]

output_map = {}
for f in files_to_parse:
    output_map[f] = (f"{f}-cobol-tree.json", f"{f}-idms-tree.json")

input_dir = "/Users/asgupta/Downloads/mbrdi-poc"
output_root_dir = "/Users/asgupta/Downloads/mbrdi-poc/parse-output"
cobol_parser_jar_path = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/engine/target/server.jar"
idms_parser_jar_path = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/idms-copybook-builder/target/idms-copybook-builder.jar"


def clean_copybook_output_dirs(out_map, out_root_dir):
    for i, o in out_map.items():
        output_dir = f"{out_root_dir}/{i}"
        if os.path.exists(output_dir) and os.path.isdir(output_dir):
            print(f"Deleting...{output_dir}")
            shutil.rmtree(output_dir)
        print(f"Creating...{output_dir}")
        os.makedirs(output_dir)


def parse_copybooks(out_map, input_dir, copybook_parser_jar_path):
    for i, o in out_map.items():
        out_dir = f"{output_root_dir}/{i}"
        print(f"Parsing {i} to {out_dir}")
        copybook_parse_output_path = f"{out_dir}/{o[1]}"
        subprocess.run(["java", "-jar", copybook_parser_jar_path, "-s", f"{input_dir}/{i}",
                        "-o", copybook_parse_output_path, "PARSE"])


clean_copybook_output_dirs(output_map, output_root_dir)
parse_copybooks(output_map, input_dir, idms_parser_jar_path)
