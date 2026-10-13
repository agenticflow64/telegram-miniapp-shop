from aiogram import Router, F
from aiogram.types import Message, InlineKeyboardMarkup
from aiogram.filters import CommandStart

from keyboards.builders import main_markup
from services.user_service import IUserService

router = Router(name="common")

@router.message(CommandStart())
async def handle_start(message: Message, user_service: IUserService):
    #_ = await user_service.register_user(
    #    telegram_id=message.from_user.id,
    #    chat_id=message.chat.id,
    #    username=message.from_user.username or ""
    #)
    await message.answer(
        "🎓 <b>Добро пожаловать в магазин мерча СГУ!</b> 🎓\n\n"
        "✨ Здесь вы найдете <i>стильные и полезные</i> товары с символикой <b>Саратовского Государственного Университета</b>:\n\n"
        "📝 <b>Канцелярия:</b> блокноты, ручки, тетради, значки и стикеры\n"
        "🛒 Выбирайте, что вам по душе, и носите гордую символику своего университета! 💙\n\n"
        "👇 <i>Что вас интересует?</i>",
        reply_markup=main_markup(),
        parse_mode="HTML"
    )
