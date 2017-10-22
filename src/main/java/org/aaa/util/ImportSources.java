package org.aaa.util;


import org.aaa.orm.entity.identifiable.IdentifiedByIdEntity;
import org.aaa.core.web.common.model.foreign.output.ToEntitiesConvertible;

import javax.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.Map;


/**
 * Created by alexandremasanes on 24/08/2017.
 */

@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "import-sources")
public final class ImportSources {

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DomainObject {

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Source {

            @XmlAccessorType(XmlAccessType.FIELD)
            public static class URI {

                @XmlAttribute
                private String path;

                @XmlAttribute
                private String host;

                @XmlAttribute
                private boolean secured;

                @XmlElementWrapper
                @XmlElement(name = "param")
                private Map<String, ?> params;

                public String getPath() {
                    return path;
                }

                public String getHost() {
                    return host;
                }

                public boolean isSecured() {
                    return secured;
                }

                public Map<String, ?> getParams() {
                    return params;
                }

                @Override
                public String toString() {
                    return "URI{" +
                            "path='" + path + '\'' +
                            ", host='"+ host+ '\'' +
                            ", secured=" + secured +
                            ", params=" + params +
                            '}';
                }
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            public static class Key {

                @XmlEnum
                public enum Mode {
                    @XmlEnumValue("in_uri")
                    IN_URI,
                    @XmlEnumValue("in_header")
                    IN_HEADER
                }

                @XmlAttribute
                private String name;

                @XmlAttribute
                private String value;

                @XmlAttribute
                private Mode mode;

                public String getName() {
                    return name;
                }

                public String getValue() {
                    return value;
                }

                public Mode getMode() {
                    return mode;
                }

                @Override
                public String toString() {
                    return "Key{" +
                            "name='" + name + '\'' +
                            ", value='" + value + '\'' +
                            ", mode=" + mode +
                            '}';
                }
            }

            @XmlAttribute
            private String name;

            private URI uri;

            private Key key;

            @XmlAttribute(name = "class")
            private Class<? extends ToEntitiesConvertible> outputClass;


            public String getName() {
                return name;
            }

            public URI getUri() {
                return uri;
            }

            public Key getKey() {
                return key;
            }

            public Class<? extends ToEntitiesConvertible> getOutputClass() {
                return outputClass;
            }

            @Override
            public String toString() {
                return "Source{" +
                        "name='" + name + '\'' +
                        ", uri=" + uri +
                        ", key=" + key +
                        ", outputClass=" + outputClass +
                        '}';
            }
        }

        @XmlAttribute(name = "class")
        private Class<? extends IdentifiedByIdEntity> subjectClass;

        @XmlElementWrapper
        @XmlElement(name = "source")
        private Source[] sources;

        public Class<? extends IdentifiedByIdEntity> getSubjectClass() {
            return subjectClass;
        }

        public Source[] getSources() {
            return sources;
        }

        @Override
        public String toString() {
            return "DomainObject{" +
                    "subjectClass=" + subjectClass +
                    ", sources=" + Arrays.toString(sources) +
                    '}';
        }
    }

    @XmlElementWrapper(name = "domain-objects")
    @XmlElement(name = "domain-object")
    private DomainObject[] domainObjects;

    public DomainObject[] getDomainObjects() {
        return domainObjects;
    }

    @Override
    public String toString() {
        return "ImportSources{" +
                "domainObjects=" + Arrays.toString(domainObjects) +
                '}';
    }
}