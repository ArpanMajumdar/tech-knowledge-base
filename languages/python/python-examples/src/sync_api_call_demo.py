from timeit import default_timer

import requests


def load_data(delay_in_sec: int) -> str:
    print(f'Starting {delay_in_sec} second timer ...')
    url = f'https://httpbin.org/delay/{delay_in_sec}'
    response = requests.get(url).text
    print(f'Completed {delay_in_sec} second timer.')
    return response


def run_demo():
    start_time = default_timer()

    load_data(2)
    load_data(3)

    elapsed_time = default_timer() - start_time
    print(f'The operation took {elapsed_time:.2} seconds')


def main():
    run_demo()


main()
