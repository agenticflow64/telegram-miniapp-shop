import asyncio
import logging
from aiogram import Bot
from dispatcher import setup_dispatcher
from config.config import settings


async def main():
    logging.basicConfig(level=logging.INFO)

    bot = Bot(token=settings.BOT_TOKEN.get_secret_value())

    dp = setup_dispatcher()

    await dp.start_polling(bot)

if __name__ == "__main__":
    asyncio.run(main())