/*
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.silverandro.rpgstats.advancemnents

import com.google.gson.JsonObject
import io.github.silverandro.rpgstats.Constants
import io.github.silverandro.rpgstats.Constants.ANY_ID
import io.github.silverandro.rpgstats.LevelUtils.getHighestLevel
import io.github.silverandro.rpgstats.advancemnents.LevelUpCriterion.LevelCriteria
import io.github.silverandro.rpgstats.stats.Components
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.class_5258
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class LevelUpCriterion : AbstractCriterion<LevelCriteria>() {
    override fun conditionsFromJson(
        obj: JsonObject,
        pred: class_5258,
        predicateDeserializer: AdvancementEntityPredicateDeserializer
    ): LevelCriteria {
        return LevelCriteria(pred, obj["level"].asInt, obj["stat"].asString)
    }

    override fun getId(): Identifier {
        return ID
    }

    fun trigger(player: ServerPlayerEntity) {
        this.trigger(player) { levelCriteria: LevelCriteria -> levelCriteria.matches(player) }
    }

    class LevelCriteria(
        playerPredicate: class_5258,
        private val level: Int,
        id: String
    ) : AbstractCriterionConditions(
        ID,
        playerPredicate
    ) {
        private val statId: Identifier

        init {
            statId = Identifier(id)
        }

        fun matches(player: ServerPlayerEntity): Boolean {
            return if (statId == ANY_ID) getHighestLevel(player) >= level else Components.STATS.get(player)
                .getOrCreateID(id).level >= level
        }

        override fun toJson(ser: AdvancementEntityPredicateSerializer): JsonObject {
            val jsonObject = super.toJson(ser)
            jsonObject.addProperty("level", level)
            jsonObject.addProperty("stat", statId.toString())
            return jsonObject
        }
    }

    companion object {
        private val ID = Identifier(Constants.MOD_ID, "player_level")
    }
}