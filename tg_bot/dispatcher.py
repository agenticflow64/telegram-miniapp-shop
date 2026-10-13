from aiogram import Dispatcher
from aiogram.dispatcher.middlewares.base import BaseMiddleware
from dependencies import user_service_factory
from handlers import common

class ServiceMiddleware(BaseMiddleware):
    async def __call__(self, handler, event, data):
        async with user_service_factory() as user_service:
            data["user_service"] = user_service
            return await handler(event, data)

def setup_dispatcher() -> Dispatcher:
    dp = Dispatcher()
    dp.update.outer_middleware(ServiceMiddleware())
    dp.include_router(common.router)
    return dp