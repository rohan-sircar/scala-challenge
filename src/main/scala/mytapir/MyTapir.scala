package mytapir

import sttp.tapir.Tapir
import sttp.tapir.TapirAliases
import sttp.tapir.codec.enumeratum.TapirCodecEnumeratum
import sttp.tapir.codec.newtype.TapirCodecNewType
import sttp.tapir.codec.refined.TapirCodecRefined
import sttp.tapir.generic.SchemaDerivation
import sttp.tapir.integ.cats.TapirCodecCats
import sttp.tapir.json.circe.TapirJsonCirce

trait MyTapir
    extends Tapir
    with TapirCodecCats
    with TapirCodecNewType
    with TapirCodecEnumeratum
    with TapirCodecRefined
    with SchemaDerivation
    with TapirJsonCirce
    with TapirAliases
