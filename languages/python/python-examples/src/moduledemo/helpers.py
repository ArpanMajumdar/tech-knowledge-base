from colorama import Fore


def display(message: str, is_warning=False):
    if is_warning:
        print(f'{Fore.RED}WARNING: {message}')
    else:
        print(f'{Fore.GREEN}INFO: {message}')
