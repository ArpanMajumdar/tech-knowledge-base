import asyncio
from timeit import default_timer

import aiohttp


async def load_data(session: aiohttp.ClientSession, delay_in_sec: int) -> str:
    print(f'Starting {delay_in_sec} second timer ...')
    url = f'https://httpbin.org/delay/{delay_in_sec}'

    async with session.get(url) as response:
        response_text = await response.text()
        print(f'Completed {delay_in_sec} second timer.')
        return response_text


async def run_demo():
    # Start the timer
    start_time = default_timer()

    # Create a aiohttp session
    async with aiohttp.ClientSession() as session:
        # Setup the tasks and run
        two_task = asyncio.create_task(load_data(session, 2))
        three_task = asyncio.create_task(load_data(session, 3))

        # Simulate other processing
        print('Doing some computation ...')
        await asyncio.sleep(1)
        print('Computation completed.')

        # Get API responses
        await two_task
        await three_task

    # Stop the timer
    elapsed_time = default_timer() - start_time
    print(f'The operation took {elapsed_time:.2} seconds')


asyncio.run(run_demo())
