package me.danny.instacrawlerkotlin.model

import me.danny.instacrawlerkotlin.model.entity.InstaAccount
import me.danny.instacrawlerkotlin.model.dto.InstaAccountDto
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 *
 * Created by danny.ban on 2019-05-24.
 *
 * @author danny.ban
 * @since
 */
open class Transformer<in T : Any, out R : Any>
protected constructor(inClass: KClass<T>, outClass: KClass<R>) {
    private val outConstructor = outClass.primaryConstructor!!
    private val inPropertiesByName by lazy {
        inClass.memberProperties.associateBy { it.name }
    }

    fun transform(data: T): R = with(outConstructor) {
        callBy(parameters.associate { parameter ->
            parameter to argFor(parameter, data)
        })
    }

    open fun argFor(parameter: KParameter, data: T): Any? {
        return inPropertiesByName[parameter.name]?.get(data)
    }
}

val instaAccountToInstaAccountDto = object : Transformer<InstaAccount, InstaAccountDto>(InstaAccount::class, InstaAccountDto::class) {}

