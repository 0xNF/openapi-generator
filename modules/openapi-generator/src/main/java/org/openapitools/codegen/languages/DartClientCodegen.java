/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 * Copyright 2018 SmartBear Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.languages;

import org.openapitools.codegen.CliOption;
import org.openapitools.codegen.CodegenComposedSchemas;
import org.openapitools.codegen.CodegenConstants;
import org.openapitools.codegen.CodegenDiscriminator;
import org.openapitools.codegen.CodegenDiscriminator.MappedModel;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DartClientCodegen extends AbstractDartCodegen {

    private final Logger LOGGER = LoggerFactory.getLogger(DartClientCodegen.class);

    public static final String SERIALIZATION_LIBRARY_NATIVE = "native_serialization";

    public DartClientCodegen() {
        super();

        final CliOption serializationLibrary = CliOption.newString(CodegenConstants.SERIALIZATION_LIBRARY,
                "Specify serialization library");
        serializationLibrary.setDefault(SERIALIZATION_LIBRARY_NATIVE);

        final Map<String, String> serializationOptions = new HashMap<>();
        serializationOptions.put(SERIALIZATION_LIBRARY_NATIVE, "Use native serializer, backwards compatible");
        serializationLibrary.setEnum(serializationOptions);
        cliOptions.add(serializationLibrary);

        sourceFolder = "";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        // handle library not being set
        if (additionalProperties.get(CodegenConstants.SERIALIZATION_LIBRARY) == null) {
            this.library = SERIALIZATION_LIBRARY_NATIVE;
            LOGGER.debug("Serialization library not set, using default {}", SERIALIZATION_LIBRARY_NATIVE);
        } else {
            this.library = additionalProperties.get(CodegenConstants.SERIALIZATION_LIBRARY).toString();
        }

        this.setSerializationLibrary();

        supportingFiles.add(new SupportingFile("pubspec.mustache", "", "pubspec.yaml"));
        supportingFiles.add(new SupportingFile("analysis_options.mustache", "", "analysis_options.yaml"));
        supportingFiles.add(new SupportingFile("api_client.mustache", libPath, "api_client.dart"));
        supportingFiles.add(new SupportingFile("api_exception.mustache", libPath, "api_exception.dart"));
        supportingFiles.add(new SupportingFile("api_helper.mustache", libPath, "api_helper.dart"));
        supportingFiles.add(new SupportingFile("apilib.mustache", libPath, "api.dart"));

        final String authFolder = libPath + "auth";
        supportingFiles.add(new SupportingFile("auth/authentication.mustache", authFolder, "authentication.dart"));
        supportingFiles.add(new SupportingFile("auth/http_basic_auth.mustache", authFolder, "http_basic_auth.dart"));
        supportingFiles.add(new SupportingFile("auth/http_bearer_auth.mustache", authFolder, "http_bearer_auth.dart"));
        supportingFiles.add(new SupportingFile("auth/api_key_auth.mustache", authFolder, "api_key_auth.dart"));
        supportingFiles.add(new SupportingFile("auth/oauth.mustache", authFolder, "oauth.dart"));
        supportingFiles.add(new SupportingFile("git_push.sh.mustache", "", "git_push.sh"));
        supportingFiles.add(new SupportingFile("gitignore.mustache", "", ".gitignore"));
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
        supportingFiles.add(new SupportingFile("travis.mustache", "", ".travis.yml"));

    }

    /// Vendor Extension key that returns Data Types as a valid Dart identifier
    ///
    /// e.g., List<String> => listLessThanStringGreaterThan
    private static final String kDataTypeAsIdentifier = "dataTypeAsIdentifier";

    /// Vendor Extension key that returns Data Types as a valid Dart Identifier in
    /// CamelCase
    ///
    /// e.g., List<String> => ListLessThanStringGreaterThan
    private static final String kDataTypeAsIdentifierCamelCase = "dataTypeAsIdentifierCamelCase";

    /// Lambda that computes Vendor Extensions for data types, by walking the Model
    /// Tree
    Consumer<CodegenProperty> addVendorDataTypeExtensions = x -> {
        x.vendorExtensions.put(kDataTypeAsIdentifier, toVarName(x.dataType));
        x.vendorExtensions.put(kDataTypeAsIdentifierCamelCase, StringUtils.camelize(toVarName(x.dataType)));

    };

    /// override the default behavior of createDiscriminator
    /// to remove extra mappings added as a side effect of
    /// setLegacyDiscriminatorBehavior(false)
    /// this ensures 1-1 schema mapping instead of 1-many
    @Override
    protected CodegenDiscriminator createDiscriminator(String schemaName, Schema schema) {
        CodegenDiscriminator sub = super.createDiscriminator(schemaName, schema);
        Discriminator originalDiscriminator = schema.getDiscriminator();
        if (originalDiscriminator != null) {
            Map<String, String> originalMapping = originalDiscriminator.getMapping();
            if (originalMapping != null && !originalMapping.isEmpty()) {
                // we already have a discriminator mapping, remove everything else
                for (MappedModel currentMappings : new HashSet<>(sub.getMappedModels())) {
                    if (originalMapping.containsKey(currentMappings.getMappingName())) {
                        // all good
                    } else {
                        sub.getMapping().remove(currentMappings.getMappingName());
                        sub.getMappedModels().remove(currentMappings);
                    }
                }
            }
        }
        return sub;
    }

    /// Visits Composed Schemas and applys the [apply] lambda to each
    /// CodegenProperty found.
    /// Primarily used to add formatted data type strings for use the Mustache files
    private void walkComposedSchemas(CodegenComposedSchemas composedSchemas, Consumer<CodegenProperty> apply) {
        if (composedSchemas != null) {
            List<CodegenProperty> schema = null;

            /* walk Any Of */
            schema = composedSchemas.getAnyOf();
            if (schema != null) {
                for (CodegenProperty cp : schema) {
                    apply.accept(cp);
                    walkComposedSchemas(cp.getComposedSchemas(), apply);
                }
            }

            /* walk One Of */
            schema = composedSchemas.getOneOf();
            if (schema != null) {
                for (CodegenProperty cp : schema) {
                    apply.accept(cp);
                    walkComposedSchemas(cp.getComposedSchemas(), apply);
                }
            }

            /* walk All Of */
            schema = composedSchemas.getAllOf();
            if (schema != null) {
                for (CodegenProperty cp : schema) {
                    apply.accept(cp);
                    walkComposedSchemas(cp.getComposedSchemas(), apply);
                }
            }

            /* walk Not */
            var not = composedSchemas.getNot();
            if (not != null) {
                apply.accept(not);
            }
        }

    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        objs = super.postProcessAllModels(objs);
        // loop through models to update the imports
        for (ModelsMap entry : objs.values()) {
            for (ModelMap mo : entry.getModels()) {
                CodegenModel cm = mo.getModel();

                cm.vendorExtensions.put(kDataTypeAsIdentifier, toVarName(cm.dataType));
                CodegenComposedSchemas cs = cm.getComposedSchemas();
                walkComposedSchemas(cs, addVendorDataTypeExtensions);

            }
        }

        return objs;
    }

    private void setSerializationLibrary() {
        final String serialization_library = getLibrary();
        LOGGER.info("Using serialization library {}", serialization_library);

        switch (serialization_library) {
            case SERIALIZATION_LIBRARY_NATIVE: // fall through to default backwards compatible generator
            default:
                additionalProperties.put(SERIALIZATION_LIBRARY_NATIVE, "true");

        }
    }
}
