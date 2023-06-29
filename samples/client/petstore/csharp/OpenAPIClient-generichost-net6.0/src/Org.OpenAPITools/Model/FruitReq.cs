// <auto-generated>
/*
 * OpenAPI Petstore
 *
 * This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\
 *
 * The version of the OpenAPI document: 1.0.0
 * Generated by: https://github.com/openapitools/openapi-generator.git
 */

using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.ComponentModel.DataAnnotations;
using OpenAPIClientUtils = Org.OpenAPITools.Client.ClientUtils;

namespace Org.OpenAPITools.Model
{
    /// <summary>
    /// FruitReq
    /// </summary>
    public partial class FruitReq : IValidatableObject
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="FruitReq" /> class.
        /// </summary>
        /// <param name="appleReq"></param>
        [JsonConstructor]
        internal FruitReq(AppleReq appleReq)
        {
            AppleReq = appleReq;
            OnCreated();
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="FruitReq" /> class.
        /// </summary>
        /// <param name="bananaReq"></param>
        [JsonConstructor]
        internal FruitReq(BananaReq bananaReq)
        {
            BananaReq = bananaReq;
            OnCreated();
        }

        partial void OnCreated();

        /// <summary>
        /// Gets or Sets AppleReq
        /// </summary>
        public AppleReq AppleReq { get; set; }

        /// <summary>
        /// Gets or Sets BananaReq
        /// </summary>
        public BananaReq BananaReq { get; set; }

        /// <summary>
        /// Returns the string presentation of the object
        /// </summary>
        /// <returns>String presentation of the object</returns>
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("class FruitReq {\n");
            sb.Append("}\n");
            return sb.ToString();
        }

        /// <summary>
        /// To validate all properties of the instance
        /// </summary>
        /// <param name="validationContext">Validation context</param>
        /// <returns>Validation Result</returns>
        IEnumerable<System.ComponentModel.DataAnnotations.ValidationResult> IValidatableObject.Validate(ValidationContext validationContext)
        {
            yield break;
        }
    }

    /// <summary>
    /// A Json converter for type <see cref="FruitReq" />
    /// </summary>
    public class FruitReqJsonConverter : JsonConverter<FruitReq>
    {
        /// <summary>
        /// Deserializes json to <see cref="FruitReq" />
        /// </summary>
        /// <param name="utf8JsonReader"></param>
        /// <param name="typeToConvert"></param>
        /// <param name="jsonSerializerOptions"></param>
        /// <returns></returns>
        /// <exception cref="JsonException"></exception>
        public override FruitReq Read(ref Utf8JsonReader utf8JsonReader, Type typeToConvert, JsonSerializerOptions jsonSerializerOptions)
        {
            int currentDepth = utf8JsonReader.CurrentDepth;

            if (utf8JsonReader.TokenType != JsonTokenType.StartObject && utf8JsonReader.TokenType != JsonTokenType.StartArray)
                throw new JsonException();

            JsonTokenType startingTokenType = utf8JsonReader.TokenType;

            while (utf8JsonReader.Read())
            {
                if (startingTokenType == JsonTokenType.StartObject && utf8JsonReader.TokenType == JsonTokenType.EndObject && currentDepth == utf8JsonReader.CurrentDepth)
                    break;

                if (startingTokenType == JsonTokenType.StartArray && utf8JsonReader.TokenType == JsonTokenType.EndArray && currentDepth == utf8JsonReader.CurrentDepth)
                    break;

                if (utf8JsonReader.TokenType == JsonTokenType.PropertyName && currentDepth == utf8JsonReader.CurrentDepth - 1)
                {
                    string propertyName = utf8JsonReader.GetString();
                    utf8JsonReader.Read();

                    switch (propertyName)
                    {
                        default:
                            break;
                    }
                }
            }

            Utf8JsonReader appleReqReader = utf8JsonReader;
            if (Client.ClientUtils.TryDeserialize<AppleReq>(ref appleReqReader, jsonSerializerOptions, out AppleReq appleReq))
                return new FruitReq(appleReq);

            Utf8JsonReader bananaReqReader = utf8JsonReader;
            if (Client.ClientUtils.TryDeserialize<BananaReq>(ref bananaReqReader, jsonSerializerOptions, out BananaReq bananaReq))
                return new FruitReq(bananaReq);

            throw new JsonException();
        }

        /// <summary>
        /// Serializes a <see cref="FruitReq" />
        /// </summary>
        /// <param name="writer"></param>
        /// <param name="fruitReq"></param>
        /// <param name="jsonSerializerOptions"></param>
        /// <exception cref="NotImplementedException"></exception>
        public override void Write(Utf8JsonWriter writer, FruitReq fruitReq, JsonSerializerOptions jsonSerializerOptions)
        {
            System.Text.Json.JsonSerializer.Serialize(writer, fruitReq.AppleReq, jsonSerializerOptions);

            System.Text.Json.JsonSerializer.Serialize(writer, fruitReq.BananaReq, jsonSerializerOptions);

            writer.WriteStartObject();

            writer.WriteEndObject();
        }
    }
}