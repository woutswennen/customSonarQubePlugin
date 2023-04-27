
package org.sonarsource.plugins.example.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RulesDefinition;

import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.INTRODUCTION_SECTION_KEY;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.ROOT_CAUSE_SECTION_KEY;

public class NoMockPolicyDefinition implements RulesDefinition {

  public static final String REPOSITORY = "terraform-rules";
  public static final String TERRAFORM_LANGUAGE = "terraform";
  public static final RuleKey NO_MOCK_POLICY = RuleKey.of(REPOSITORY, "no-mock-policy");
  
  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(REPOSITORY, TERRAFORM_LANGUAGE).setName("Custom Terraform Mock Policy Analyzer");
     
    NewRule noMockRule = repository.createRule(NO_MOCK_POLICY.rule())
      .setName("Azure Policy can not contain Mock policy")
      .setHtmlDescription("Generates an issue whenever there is a <mock-policy> defined in terraform")
      .addDescriptionSection(descriptionSection(INTRODUCTION_SECTION_KEY, "We can not have the mock the response defined in terraform.", null))
      .addDescriptionSection(descriptionSection(ROOT_CAUSE_SECTION_KEY, "There can be no <mock-response> tag in your terraform code.", null))
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
