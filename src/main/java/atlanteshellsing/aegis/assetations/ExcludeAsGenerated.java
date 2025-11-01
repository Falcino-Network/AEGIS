package atlanteshellsing.aegis.assetations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS) // no runtime overhead needed
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ExcludeAsGenerated { }
