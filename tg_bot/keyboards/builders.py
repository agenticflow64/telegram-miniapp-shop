from aiogram.utils.keyboard import InlineKeyboardBuilder
from aiogram.types import WebAppInfo

from config.config import settings


def main_markup():
    builder = InlineKeyboardBuilder()
    builder.button(
        text="Запустить приложение",
        web_app=WebAppInfo(url=settings.WEBAPP_URL)
    )
    return builder.as_markup()