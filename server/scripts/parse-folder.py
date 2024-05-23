import os
import subprocess
import shutil

copybooks = ["V75CWEUR", "V75CW88A", "V75CW880", "V75CW201", "V75CW129", "V75CW124", "V75CW123",
             "V75CW022", "V75CW013", "V75CW002", "V75CP88A", "V75CP202", "V75CP201", "V75CP129",
             "V75CP124", "V75CP123", "V75CP011"]
# programs = ["V7523438-compiled", "V751C931", "V7588049", "V75234"]
# programs = ["V7523438-compiled", "V751C931", "V7588049", "V75234"]
programs = ["V751C931", "V7588049", "V75234"]
# programs = ["V75234"]


def output_map(filenames):
    out_map = {}
    for f in filenames:
        out_map[f] = (f"{f}-cobol-tree.json", f"{f}-idms-tree.json")
    return out_map


input_dir = "/Users/asgupta/Downloads/mbrdi-poc"
output_root_dir = "/Users/asgupta/Downloads/mbrdi-poc/parse-output"
cobol_parser_jar_path = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/engine/target/server.jar"
copybook_parser_jar_path = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/idms-copybook-builder/target/idms-copybook-builder.jar"


def clean_output_dirs(out_map, out_root_dir):
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


def parse_programs(out_map, input_dir, copybook_dir, cobol_parser_jar_path):
    for i, o in out_map.items():
        out_dir = f"{output_root_dir}/{i}"
        print(f"Parsing {i} to {out_dir}")
        program_parse_output_path = f"{out_dir}/{o[0]}"
        copybook_parse_output_path = f"{out_dir}/{o[1]}"
        subprocess.run(["java", "-jar", cobol_parser_jar_path, "-s", f"{input_dir}/{i}",
                        "-oidms", copybook_parse_output_path,
                        "-ocobol", program_parse_output_path,
                        "analysis",
                        "-cf", copybook_dir])


# Parse copybooks
out_map_copybooks = output_map(copybooks)
clean_output_dirs(out_map_copybooks, output_root_dir)
parse_copybooks(out_map_copybooks, input_dir, copybook_parser_jar_path)

# # Parse programs
out_map_programs = output_map(programs)
clean_output_dirs(out_map_programs, output_root_dir)
parse_programs(out_map_programs, input_dir, input_dir, cobol_parser_jar_path)
