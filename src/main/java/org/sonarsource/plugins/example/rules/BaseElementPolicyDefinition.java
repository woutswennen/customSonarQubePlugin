
package org.sonarsource.plugins.example.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RulesDefinition;

import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.INTRODUCTION_SECTION_KEY;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.ROOT_CAUSE_SECTION_KEY;

public class BaseElementPolicyDefinition implements RulesDefinition {

  public static final String REPOSITORY = "terraform-rules";
  public static final String TERRAFORM_LANGUAGE = "terraform";
  public static final RuleKey BASE_TAG_IN_POLICY = RuleKey.of(REPOSITORY, "base-element");

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY, TERRAFORM_LANGUAGE).setName("Custom Terraform Base Element Policy Analyzer");

    NewRule baseInPolicies = repository.createRule(BASE_TAG_IN_POLICY.rule())
      .setName("Every element of policies should have a base element.")
      .setHtmlDescription("Generates an issue whenever there is no Base Element defined in <inbound>, <outbound>, <backend> or <on-error>")
      .addDescriptionSection(descriptionSection(INTRODUCTION_SECTION_KEY, "We can not have a policies element without a base element.", null))
      .addDescriptionSection(descriptionSection(ROOT_CAUSE_SECTION_KEY, "The <base> tag should be in every <inbound>, <outbound>, <backend> and <on-erro> tag.", null))
      .setTags("azure", "policy")

      // optional status. Default value is READY.
      .setStatus(RuleStatus.BETA)

      // default severity when the rule is activated on a Quality profile. Default value is MAJOR.
      .setSeverity(Severity.MAJOR);

    // don't forget to call done() to finalize the definition
    repository.done();
  }

  private static RuleDescriptionSection descriptionSection(String sectionKey, String htmlContent, org.sonar.api.server.rule.Context context) {
    return RuleDescriptionSection.builder()
      .sectionKey(sectionKey)
      .htmlContent(htmlContent)
      .context(context)
      .build();
  }
}
