from tyk.decorators import *
from gateway import TykGateway as tyk

@Hook
def PreHook(request, session, spec):
    tyk.log("PreHook is called", "info")
    v = tyk.get_data('a_key')
    tyk.log("v=" + v, "info")
    return request, session
